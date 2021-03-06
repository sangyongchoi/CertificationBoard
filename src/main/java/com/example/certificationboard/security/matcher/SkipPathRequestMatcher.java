package com.example.certificationboard.security.matcher;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkipPathRequestMatcher implements RequestMatcher {

    private OrRequestMatcher skipRequestMatcher;

    public SkipPathRequestMatcher(String... skipPathList) {
        List<RequestMatcher> requestMatcherList = Stream.of(skipPathList)
                .map(AntPathRequestMatcher::new).collect(Collectors.toList());
        skipRequestMatcher = new OrRequestMatcher(requestMatcherList);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return !skipRequestMatcher.matches(request);
    }

}
