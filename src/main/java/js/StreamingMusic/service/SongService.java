package js.StreamingMusic.service;

import js.StreamingMusic.domain.Song;
import js.StreamingMusic.domain.SongDto;
import js.StreamingMusic.exception.DuplicateSongException;
import js.StreamingMusic.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongService {

    private final SongRepository songRepository;


    @Transactional
    public void addSong(Song song, String name, String title, String artist) {
        validateDuplicateSong(song, name, title, artist);
        songRepository.save(song);
    }


    @Transactional
    public void removeSong(Song song) {
        songRepository.remove(song);
    }

    public Song findSong(Long songid) {
        return songRepository.findOne(songid);
    }

//    public List<Song> findAllSongsByName(String name) {
//        return songRepository.findAllByName(name);
//    }

    public List<SongDto> findAllSongsByName(String name) {
        return songRepository.findAllByName(name);
    }



    private void validateDuplicateSong(Song song, String name, String title, String artist) {
        List<Song> duplicateSong = songRepository.findDuplicateSong(song, name, title, artist);
        if (!duplicateSong.isEmpty()) {
            throw new DuplicateSongException("이미 존재하는 노래입니다");
        }
    }


    public List<SongDto> findAllSongsByCategory(String name, String genre) {
        return songRepository.findAllByCategory(name, genre);
    }

}
