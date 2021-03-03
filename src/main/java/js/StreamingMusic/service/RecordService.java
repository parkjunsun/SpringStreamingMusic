package js.StreamingMusic.service;

import js.StreamingMusic.domain.entity.Record;
import js.StreamingMusic.domain.dto.RecordDto;
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
    public void addRecord(Record record) {
        recordRepository.save(record);
    }


    public boolean findDuplicateRecord(String username, String title, String artist) {
        List<Record> duplicateRecord = recordRepository.findDuplicateRecord(username, title, artist);
        if (!duplicateRecord.isEmpty()) {
            return false;
        }
        return true;
    }


    @Transactional
    public void addRecordCount(String username, String title, String artist) {
        Record record = recordRepository.findDuplicateRecord(username, title, artist).get(0);
        record.addCount(1);
    }

    public List<Record> findAll(String username) {
        return recordRepository.findByUserName(username);
    }


    public List<RecordDto> findMostPlayCountByArtist(String name) {
        return recordRepository.findMostArtist(name);
    }


}
