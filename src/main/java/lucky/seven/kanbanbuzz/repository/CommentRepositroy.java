package lucky.seven.kanbanbuzz.repository;

import lucky.seven.kanbanbuzz.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepositroy extends JpaRepository<Comment,Long> {

}
