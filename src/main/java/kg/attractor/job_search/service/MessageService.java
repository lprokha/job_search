package kg.attractor.job_search.service;

import kg.attractor.job_search.model.Message;
import kg.attractor.job_search.model.User;

import java.util.List;

public interface MessageService {

    Message create(Integer respondedApplicantId, String content, User sender);

    List<Message> getByRespondedApplicantId(Integer respondedApplicantId);
}