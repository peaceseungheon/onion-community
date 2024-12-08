package com.onion.backend.service;

import com.onion.backend.entity.Board;
import com.onion.backend.repository.BoardRepository;
import java.util.List;
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

}
