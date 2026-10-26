package com.msintro.resource_processor.services;

import com.msintro.resource_processor.dto.MetadataDto;
import com.msintro.resource_processor.exceptions.InvalidMP3FormatException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;

import static com.msintro.resource_processor.constants.Constants.*;

@Service
public class ResourceService {

    public MetadataDto process(byte[] audioData) {
        MetadataDto metadata = parseAudio(audioData);
        if (metadata == null) throw new InvalidMP3FormatException();
        return metadata;
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
