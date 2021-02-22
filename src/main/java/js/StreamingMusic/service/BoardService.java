package js.StreamingMusic.service;

import js.StreamingMusic.domain.Board;
import js.StreamingMusic.exception.BoardInputEmptyException;
import js.StreamingMusic.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void savePost(Board board) {
        validateBoardInputEmpty(board);
        boardRepository.save(board);
    }

    @Transactional
    public void removePost(Board board) {
        boardRepository.remove(board);
    }

    public List<Board> getBoardList(String album_id) {
        return boardRepository.findByAlbumId(album_id);
    }

    public Board getBoard(Long id) {
        return boardRepository.findOne(id);
    }

    private void validateBoardInputEmpty(Board board) {
        if (board.getComment().isEmpty()) {
            throw new BoardInputEmptyException("댓글을 입력해 주세요");
        }
    }

}
