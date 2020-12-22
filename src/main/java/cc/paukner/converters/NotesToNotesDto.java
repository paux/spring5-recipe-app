package cc.paukner.converters;

import cc.paukner.domain.Notes;
import cc.paukner.dtos.NotesDto;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesToNotesDto implements Converter<Notes, NotesDto> {

    @Synchronized
    @Nullable
    @Override
    public NotesDto convert(Notes notes) {
        if (null == notes) {
            return null;
        }

        return NotesDto.builder()
                .id(notes.getId())
                .content(notes.getContent())
                .build();
    }
}
