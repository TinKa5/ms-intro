package com.msintro.resource_service.services;

import com.msintro.resource_service.dto.IdListResponseDto;
import com.msintro.resource_service.dto.IdResponseDto;
import com.msintro.resource_service.dto.MetadataDto;
import com.msintro.resource_service.exceptions.*;
import com.msintro.resource_service.repository.AudioRecord;
import com.msintro.resource_service.repository.AudioRecordRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.msintro.resource_service.constants.Constants.*;

@Service
@Data
public class ResourceService {

    private final AudioRecordRepository repository;
    private final SongRestClient client;

    public IdResponseDto save(byte[] audioData) {

        MetadataDto metadata = parseAudio(audioData);
        if (metadata == null) throw new InvalidMP3FormatException();

        AudioRecord record = repository.save(new AudioRecord(null, "audio/mpeg", audioData));

        metadata.setId(record.getId());
        if (client != null) {
            HttpStatusCode status = client.saveMetadata(metadata).getStatusCode();
            if (status.isError()) {
                repository.deleteById(record.getId());
                throw new InvalidMP3FormatException();
            }
        }
        return new IdResponseDto(record.getId());
    }

    public byte[] get(String idStr) {
        Long id = parseId(idStr);
        AudioRecord record = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id));
        return record.getAudioData();
    }

    public IdListResponseDto delete(String id) {
        if (id.isBlank()) return new IdListResponseDto(new Long[]{});
        Collection<Long> ids = parseIds(id);
        Collection<Long> result = ids.stream()
                .filter(repository::existsById)
                .toList();
        repository.deleteAllById(result);

        String queryString = result.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        client.deleteMetadata(queryString);
        return new IdListResponseDto(result.toArray(Long[]::new));
    }

    public Collection<Long> parseIds(String str) {
        if (str != null && str.length() > 200) throw new InvalidLengthIdListException(str.length());
        List<Long> result = new ArrayList<>();

        Arrays.stream(str.split(","))
                .forEach(id -> {
                    try {
                        result.add(Long.valueOf(id));
                    } catch (NumberFormatException e) {
                        throw new InvalidIdListException(id);
                    }
                });
        return result;
    }

    private Long parseId(String str) {
        try {
            Long result = Long.valueOf(str);
            if (result > 0) return result;
        } catch (NumberFormatException ex) {

        }
        throw new InvalidIdException(str);
    }

    private MetadataDto parseAudio(byte[] audioData) {
        try (TikaInputStream inputStream = TikaInputStream.get(audioData)) {
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();

            Mp3Parser Mp3Parser = new Mp3Parser();
            Mp3Parser.parse(inputStream, handler, metadata, context);
            return mapMetadata(metadata);
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException(e);
        }

    }

    private MetadataDto mapMetadata(Metadata metadata) {
        if (!isValidMetadata(metadata)) return null;
        int total;
        try {
            total = (int) Double.parseDouble(metadata.get(DURATION));
        } catch (NumberFormatException ex) {
            return null;
        }

        String duration = String.format("%02d:%02d", total / 60, total % 60);
        return MetadataDto.builder()
                .name(metadata.get(NAME))
                .album(metadata.get(ALBUM))
                .artist(metadata.get(ARTIST))
                .year(metadata.get(YEAR))
                .duration(duration)
                .build();
    }

    private boolean isValidMetadata(Metadata metadata) {
        if (StringUtils.isBlank(metadata.get(NAME))
                || StringUtils.isBlank(metadata.get(ALBUM))
                || StringUtils.isBlank(metadata.get(ARTIST))
                || StringUtils.isBlank(metadata.get(YEAR))
                || StringUtils.isBlank(metadata.get(DURATION))) {
            return false;
        }
        return true;
    }

}
