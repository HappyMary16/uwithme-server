package com.mborodin.uwm.client.config;

import static org.apache.http.HttpHeaders.CONTENT_LENGTH;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mborodin.uwm.api.studcab.Credentials;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

/**
 * It was added just because of Stud cabinet specifics
 */
@RequiredArgsConstructor
public class StudCabInterceptor implements RequestInterceptor {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public void apply(final RequestTemplate template) {
        final var credentials = objectMapper.readValue(template.body(), Credentials.class);

        final var body = new RequestBody(credentials).toBuilder()
                                                     .page(getQueryParam(template, "page"))
                                                     .semestr(getQueryParam(template, "semester"))
                                                     .build();

        final String jsonBody = objectMapper.writeValueAsString(body);
        template.body(jsonBody);
        template.header(CONTENT_TYPE, "application/json");
        template.header(CONTENT_LENGTH, String.valueOf(jsonBody.getBytes().length));
    }

    private String getQueryParam(final RequestTemplate template, final String paramName) {
        return Optional.of(template)
                       .map(RequestTemplate::queries)
                       .map(map -> map.get(paramName))
                       .filter(Predicate.not(CollectionUtils::isEmpty))
                       .map(Collection::iterator)
                       .map(Iterator::next)
                       .map(param -> URLDecoder.decode(param, StandardCharsets.UTF_8))
                       .orElse(null);
    }

    @Data
    @Builder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RequestBody {

        private String email;
        private String pass;
        private int[] marks;
        private String page;
        private String semestr;
        private String fio_student = "快是慢，慢是快";

        public RequestBody(final Credentials credentials) {
            this.email = credentials.getEmail();
            this.pass = credentials.getPassword();
            this.marks = credentials.getMarks();
        }

        public String getSemester() {
            return semestr;
        }
    }
}
