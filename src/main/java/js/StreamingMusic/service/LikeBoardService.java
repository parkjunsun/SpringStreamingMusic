package js.StreamingMusic.service;

import js.StreamingMusic.domain.entity.LikeBoard;
import js.StreamingMusic.repository.LikeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeBoardService {

    private final LikeBoardRepository likeBoardRepository;

    @Transactional
    public void saveLike(LikeBoard likeBoard) {
        likeBoardRepository.save(likeBoard);
    }

    public List<LikeBoard> findLikeMarking(String name) {
        return likeBoardRepository.findByUserName(name);
    }
}
