package dev.backlog.domain.post.service;

import dev.backlog.domain.hashtag.infrastructure.persistence.HashtagJpaRepository;
import dev.backlog.domain.hashtag.model.Hashtag;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.PostHashtag;
import dev.backlog.domain.post.model.repository.PostHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostHashtagService {

    private final PostHashtagRepository postHashtagRepository;
    private final HashtagJpaRepository hashtagJpaRepository;

    public void save(Set<String> names, Post post) {
        List<Hashtag> hashtags = findHashtags(names);
        List<PostHashtag> postHashtags = hashtags.stream()
                .map(hashtag -> new PostHashtag(hashtag, post))
                .toList();

        postHashtagRepository.saveAll(postHashtags);
    }

    public void deleteAllByPost(Post post) {
        List<PostHashtag> postHashtags = postHashtagRepository.findByPost(post);

        List<Hashtag> hashtags = postHashtags.stream()
                .map(PostHashtag::getHashtag)
                .toList();

        postHashtagRepository.deleteAllByPost(post);
        for (Hashtag hashtag : hashtags) {
            if (!postHashtagRepository.existsByHashtag(hashtag)) {
                hashtagJpaRepository.delete(hashtag);
            }
        }
    }

    private List<Hashtag> findHashtags(Set<String> hashtags) {
        return hashtags.stream()
                .map(this::findOrCreate)
                .toList();
    }

    private Hashtag findOrCreate(String hashtag) {
        return hashtagJpaRepository.findByName(hashtag)
                .orElseGet(() -> hashtagJpaRepository.save(new Hashtag(hashtag)));
    }

}
