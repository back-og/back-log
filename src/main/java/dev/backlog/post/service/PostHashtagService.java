package dev.backlog.post.service;

import dev.backlog.post.domain.Hashtag;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.PostHashtag;
import dev.backlog.post.domain.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@Service
@RequiredArgsConstructor
public class PostHashtagService {

    private final HashtagRepository hashtagRepository;

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
        return hashtagRepository.findByName(hashtag)
                .orElseGet(() -> hashtagRepository.save(new Hashtag(hashtag)));
    }

}
