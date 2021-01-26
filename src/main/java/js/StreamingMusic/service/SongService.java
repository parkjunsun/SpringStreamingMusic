package js.StreamingMusic.service;

import js.StreamingMusic.domain.Song;
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
    public void addSong(Song song) {
        validateDuplicateSong(song);
        songRepository.save(song);
    }

    public List<Song> findAllSongsByName(String name) {
        return songRepository.findAllByName(name);
    }

    private void validateDuplicateSong(Song song) {
        List<Song> findSongsByTitle = songRepository.findAllByTitle(song.getTitle());
        List<Song> findSongsByArtist = songRepository.findAllByArtist(song.getArtist());
        List<Song> findSongByName = songRepository.findAllByName(song.getMember().getUsername());
        if (!findSongsByTitle.isEmpty() && !findSongsByArtist.isEmpty() && !findSongByName.isEmpty()) {
            throw new DuplicateSongException("이미 존재하는 노래입니다");
        }
    }

}
