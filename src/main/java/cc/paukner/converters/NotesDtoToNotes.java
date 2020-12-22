package cc.paukner.converters;

import cc.paukner.domain.Notes;
import cc.paukner.dtos.NotesDto;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesDtoToNotes implements Converter<NotesDto, Notes> {

    @Synchronized
    @Nullable
    @Override
    public Notes convert(NotesDto notesDto) {
        if (null == notesDto) {
            return null;
        }

        return Notes.builder()
                .id(notesDto.getId())
                .content(notesDto.getContent())
                .build();
    }
}
