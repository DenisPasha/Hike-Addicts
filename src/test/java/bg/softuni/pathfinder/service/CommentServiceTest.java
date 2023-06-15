package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.view.CommentsView;
import bg.softuni.pathfinder.model.entities.Comments;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.repository.CommentRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServiceTest {

    private CommentRepository commentRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private CommentService commentService;


    @BeforeEach
    public void setUp() throws Exception {
        userService = Mockito.mock(UserService.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        modelMapper = new ModelMapper();

        commentService = new CommentService(commentRepository , userService ,modelMapper);
    }

    @Test
    public void saveCommentTest() {
        Principal mock = Mockito.mock(Principal.class);

        User user = new User();
        user.setUsername("testUser");
        user.setId(1L);
        user.setPassword("password");
        when(userService.getUserByUserName(any())).thenReturn(user);

        ArgumentCaptor<Comments> captor = ArgumentCaptor.forClass(Comments.class);
        commentService.saveComment(1L, "test comment", mock);
        verify(commentRepository).save(any());

    }

    @Test
    public void getAllCommentsForRouteTest(){
        Comments comments = new Comments();
        comments.setRouteId(1L);
        comments.setText("some text");
        comments.setAuthor(Mockito.mock(User.class));
        comments.setDateTime(LocalDateTime.now());

        Comments comments2 = new Comments();
        comments2.setRouteId(1L);
        comments2.setText("some text");
        comments2.setAuthor(Mockito.mock(User.class));
        comments2.setDateTime(LocalDateTime.now());

        List<Comments> commentList = new ArrayList<>();
        commentList.add(comments);
        commentList.add(comments2);
        when(commentRepository.findAllByRouteId(any(Long.class))).thenReturn(commentList);
        List<CommentsView> allCommentsForRoute = commentService.getAllCommentsForRoute(1L);

        assertThat(allCommentsForRoute.get(0).getAuthor()).isEqualTo(comments.getAuthor());
        assertThat(allCommentsForRoute.get(1).getAuthor()).isEqualTo(comments2.getAuthor());


    }

}