package lucky.seven.kanbanbuzz.repository;

import lucky.seven.kanbanbuzz.entity.Card;
import lucky.seven.kanbanbuzz.entity.Comment;
import lucky.seven.kanbanbuzz.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepositroy extends JpaRepository<Comment,Long> {
    @Cacheable(value = "comments", key = "#card.id + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    Page<Comment> findByCard(Card card, Pageable pageable);
}
