package dev.backlog.domain.post.service;

import dev.backlog.domain.hashtag.infrastructure.persistence.HashtagJpaRepository;
import dev.backlog.domain.hashtag.model.Hashtag;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.PostHashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@Service
@RequiredArgsConstructor
public class PostHashtagService {

    private final HashtagJpaRepository hashtagJpaRepository;

    public void associatePostWithHashtags(List<String> names, Post post) {
        if (names == null || names.isEmpty()) {
            post.removeAllHashtag();
            return;
        }
        Set<String> hashtags = removeDuplicates(names);
        List<PostHashtag> postHashtags = hashtags.stream()
                .map(name -> new PostHashtag(findOrCreate(name), post))
                .toList();
        post.addAllPostHashtag(postHashtags);
    }

    private Set<String> removeDuplicates(List<String> names) {
        return names.stream()
                .collect(toCollection(LinkedHashSet::new));
    }

    private Hashtag findOrCreate(String hashtag) {
        return hashtagJpaRepository.findByName(hashtag)
                .orElseGet(() -> hashtagJpaRepository.save(new Hashtag(hashtag)));
    }

}
