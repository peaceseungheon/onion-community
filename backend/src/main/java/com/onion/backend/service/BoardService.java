package com.onion.backend.service;

import com.onion.backend.entity.Board;
import com.onion.backend.repository.BoardRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> fetchAllBoards(){
        return boardRepository.findAll();
    }

    public boolean existBoard(Long boardId){
        Optional<Board> opBoard = boardRepository.findById(boardId);
        return opBoard.isPresent();
    }

}
