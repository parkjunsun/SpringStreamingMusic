package js.StreamingMusic.service;

import js.StreamingMusic.domain.Board;
import js.StreamingMusic.exception.BoardInputEmptyException;
import js.StreamingMusic.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private static final int BLOCK_PAGE_NUM_COUNT = 10;
    private static final int PAGE_POST_COUNT = 20;

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



    public List<Board> findBoardList(Integer pageNum, String traceId) {
        return boardRepository.findByAlbumId(pageNum, traceId, PAGE_POST_COUNT);
    }


    public Board findBoard(Long id) {
        return boardRepository.findOne(id);
    }


    private void validateBoardInputEmpty(Board board) {
        if (board.getComment().isEmpty()) {
            throw new BoardInputEmptyException("댓글을 입력해 주세요");
        }
    }


    public List<Board> findBoardByUserName(String name) {
        return boardRepository.findByUsername(name);
    }



    public List<Integer> getPageList(Integer curPageNum, String traceId) {
        List<Integer> pageList = new ArrayList<>();
        Double postsTotalCount = Double.valueOf(this.getBoardCount(traceId));

        Integer totalLastPageNum = (int)(Math.ceil(postsTotalCount/PAGE_POST_COUNT));

        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

//        for (int val=1,i=0; val<=blockLastPageNum; val++, i++) pageList[i] = val;

        if (totalLastPageNum > 10) {
            for (int i = 0; i < 10; i++) pageList.add(i+1);
        } else {
            for (int i = 0; i < totalLastPageNum; i++) pageList.add(i+1);
        }

        return pageList;
    }

    public Long getBoardCount(String traceId) {
        return boardRepository.count(traceId);
    }

}
