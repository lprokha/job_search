<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">

            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="mb-3">${vacancy.name}</h2>

                    <p class="mb-2">
                        <span class="fw-semibold"><@spring.message "vacancy.salary"/>:</span>
                        ${vacancy.salary!0}
                    </p>

                    <p class="mb-2">
                        <span class="fw-semibold"><@spring.message "vacancy.experience"/>:</span>
                        ${vacancy.expFrom!0} - ${vacancy.expTo!0}
                        <@spring.message "vacancy.years"/>
                    </p>

                    <p class="mb-2">
                        <span class="fw-semibold"><@spring.message "label.category"/>:</span>
                        ${(vacancy.category.name)!""}
                    </p>

                    <p class="mb-2">
                        <span class="fw-semibold"><@spring.message "vacancy.updated"/>:</span>
                        ${updatedAt!""}
                    </p>

                    <#if vacancy.author??>
                        <p class="mb-2">
                            <span class="fw-semibold"><@spring.message "company.title"/>:</span>
                            <a href="/companies/${vacancy.author.id}"
                               class="link-dark fw-semibold text-decoration-underline">
                                ${vacancy.author.name} ${vacancy.author.surname}
                            </a>
                        </p>
                    </#if>

                    <hr>

                    <h5 class="mb-2">
                        <@spring.message "vacancy.description"/>
                    </h5>

                    <p>${vacancy.description!""}</p>

                    <div class="d-flex gap-2 mt-4">
                        <a href="/vacancies" class="btn btn-outline-dark">
                            <@spring.message "button.back"/>
                        </a>

                        <#if currentUser?? && currentUser.accountType?string == "APPLICANT">
                            <#if resumes?? && (resumes?size > 0)>
                                <button type="button"
                                        class="btn btn-dark"
                                        data-bs-toggle="modal"
                                        data-bs-target="#respondModal">
                                    <@spring.message "button.respond"/>
                                </button>
                            <#else>
                                <a href="/resumes/create" class="btn btn-dark">
                                    <@spring.message "resume.createTitle"/>
                                </a>
                            </#if>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <#if currentUser?? && currentUser.accountType?string == "APPLICANT" && resumes?? && (resumes?size > 0)>
        <div class="modal fade" id="respondModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="/vacancies/${vacancy.id}/respond" method="post">
                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!''}">
                        </#if>

                        <input type="hidden" name="vacancyId" value="${vacancy.id}">

                        <div class="modal-header">
                            <h5 class="modal-title">
                                <@spring.message "response.chooseResume"/>
                            </h5>

                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <label class="form-label">
                                <@spring.message "nav.myResumes"/>
                            </label>

                            <select name="resumeId" class="form-select" required>
                                <#list resumes as resume>
                                    <option value="${resume.id}">
                                        ${resume.name}
                                    </option>
                                </#list>
                            </select>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                                <@spring.message "button.cancel"/>
                            </button>

                            <button type="submit" class="btn btn-dark">
                                <@spring.message "button.respond"/>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </#if>
</@main.layout>