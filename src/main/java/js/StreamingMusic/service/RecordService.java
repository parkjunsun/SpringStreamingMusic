package js.StreamingMusic.service;

import js.StreamingMusic.domain.Record;
import js.StreamingMusic.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional
    public void addRecord(Record record, String username, String title, String artist) {
        if (validDuplicateRecord(record, username, title, artist)) {
            recordRepository.save(record);
        }
    }

    private boolean validDuplicateRecord(Record record, String username, String title, String artist) {
        List<Record> duplicateRecord = recordRepository.findDuplicateRecord(record, username, title, artist);
        if (!duplicateRecord.isEmpty()) {
            record.addCount(1);
            return false;
        }

        return true;
    }
}
