package pl.lottojjr.domain.resultannouncer;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ResponseRepositoryTestImpl implements ResponseRepository {
    @Override
    public ResultResponse save(ResultResponse resultResponse) {
        return null;
    }

    @Override
    public boolean existsById(String hash) {
        return false;
    }

    @Override
    public ResultResponse findByHash(String hash) {
        return null;
    }

    @Override
    public <S extends ResultResponse> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ResultResponse> findById(String s) {
        return Optional.empty();
    }

    @Override
    public List<ResultResponse> findAll() {
        return null;
    }

    @Override
    public Iterable<ResultResponse> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(ResultResponse entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ResultResponse> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ResultResponse> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ResultResponse> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResultResponse> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends ResultResponse> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends ResultResponse> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ResultResponse> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ResultResponse> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ResultResponse> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResultResponse> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ResultResponse> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ResultResponse, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
