package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.view.CommentsView;
import bg.softuni.pathfinder.model.entities.Comments;
import bg.softuni.pathfinder.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;


    public CommentService(CommentRepository commentRepository, UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void saveComment(Long id, String message, Principal principal) {
        Comments comments = initiateComment(id, message, principal);
        commentRepository.save(comments);
    }

    private Comments initiateComment(Long id, String message, Principal principal) {
        Comments comments = new Comments();
        comments.setAuthor(userService.getUserByUserName(principal.getName()));
        comments.setText(message);
        comments.setDateTime(LocalDateTime.now());
        comments.setRouteId(id);
        return comments;
    }

    public List<CommentsView> getAllCommentsForRoute(Long id) {

        List<CommentsView> commentsViews = mapToCommentView(id);
        return commentsViews;
    }

    private List<CommentsView> mapToCommentView(Long id) {
        List<CommentsView> commentsViews = new ArrayList<>();

        commentRepository.findAllByRouteId(id).forEach(comment -> {
            CommentsView commentsView = new CommentsView();
            commentsView.setAuthor(comment.getAuthor());
            commentsView.setText(comment.getText());
            commentsView.setDateTime(comment.getDateTime().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
            commentsViews.add(commentsView);
        });
        return commentsViews;
    }
}
