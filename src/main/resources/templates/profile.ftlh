<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body">
            <h2>${user.name} ${user.surname}</h2>

            <p class="text-muted mb-1">${user.email}</p>
            <p class="mb-1">${user.phoneNumber!""}</p>

            <span class="badge text-bg-secondary">
                <#if user.accountType?string == "APPLICANT">
                    <@spring.message "profile.applicant"/>
                <#else>
                    <@spring.message "profile.employer"/>
                </#if>
            </span>

            <div class="mt-3">
                <p class="mb-2">
                    <strong><@spring.message "profile.currentPhoto"/>:</strong>
                </p>

                <#if user.avatar?? && user.avatar?has_content && user.avatar != "default-avatar.png">
                    <div class="mt-2">
                        <img src="/uploads/avatars/${user.avatar}"
                             alt="Avatar"
                             class="rounded"
                             style="max-width: 150px;">
                    </div>
                <#else>
                    <p class="text-muted mb-2">
                        <@spring.message "profile.notSet"/>
                    </p>
                </#if>

                <form action="/profile/avatar"
                      method="post"
                      enctype="multipart/form-data">

                    <#if _csrf??>
                        <input
                                type="hidden"
                                name="${(_csrf.parameterName)!'csrf'}"
                                value="${(_csrf.token)!''}">
                    </#if>

                    <div class="row g-2 align-items-center">
                        <div class="col-md-8">
                            <input type="file"
                                   name="file"
                                   class="form-control"
                                   accept="image/*">
                        </div>

                        <div class="col-md-4">
                            <button type="submit"
                                    class="btn btn-outline-dark w-100">
                                <@spring.message "profile.uploadPhoto"/>
                            </button>
                        </div>
                    </div>
                </form>

                <#if avatarError??>
                    <div class="text-danger small mt-2">
                        ${avatarError}
                    </div>
                </#if>
            </div>
        </div>
    </div>

    <div class="mb-4 d-flex gap-2">
        <a href="/profile/edit" class="btn btn-dark btn-sm">
            <@spring.message "profile.edit"/>
        </a>

        <#if user.accountType?string == "APPLICANT">
            <a href="/responses/my" class="btn btn-outline-dark btn-sm">
                <@spring.message "responses.myTitle"/>
            </a>
        </#if>
    </div>

    <#if user.accountType?string == "APPLICANT">

        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="mb-0">
                <@spring.message "resumes.myTitle"/>
            </h3>

            <div class="d-flex gap-2">
                <a href="/resumes/create" class="btn btn-dark btn-sm">
                    <@spring.message "button.add"/>
                </a>

                <a href="/resumes" class="btn btn-outline-dark btn-sm">
                    <@spring.message "button.viewAll"/>
                </a>
            </div>
        </div>

        <#if resumes?? && (resumes?size > 0)>
            <div class="list-group shadow-sm">
                <#list resumes as resume>
                    <div class="list-group-item">
                        <div class="d-flex justify-content-between align-items-start gap-3">
                            <div>
                                <h5 class="mb-2">${resume.name}</h5>

                                <p class="mb-1">
                                    <@spring.message "vacancy.salary"/>:
                                    ${resume.salary!0}
                                </p>

                                <p class="text-muted mb-0">
                                    <@spring.message "vacancy.updated"/>:
                                    ${resumeUpdateTimes[resume.id?string]!""}
                                </p>
                            </div>

                            <div class="d-flex gap-2 text-nowrap">
                                <a href="/responses/my"
                                   class="btn btn-outline-dark btn-sm">
                                    <@spring.message "button.responses"/>
                                </a>

                                <a href="/resumes/edit/${resume.id}"
                                   class="btn btn-outline-dark btn-sm">
                                    <@spring.message "button.edit"/>
                                </a>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="alert alert-light border">
                <@spring.message "profile.noResumes"/>
            </div>
        </#if>

    <#else>

        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="mb-0">
                <@spring.message "nav.myVacancies"/>
            </h3>

            <div class="d-flex gap-2">
                <a href="/my-vacancies/create" class="btn btn-dark btn-sm">
                    <@spring.message "button.add"/>
                </a>

                <a href="/my-vacancies" class="btn btn-outline-dark btn-sm">
                    <@spring.message "button.viewAll"/>
                </a>
            </div>
        </div>

        <#if vacancies?? && (vacancies?size > 0)>
            <div class="list-group shadow-sm">
                <#list vacancies as vacancy>
                    <div class="list-group-item list-group-item-action position-relative">
                        <div class="d-flex justify-content-between align-items-start gap-3">
                            <div>
                                <h5 class="mb-2">
                                    <a href="/vacancies/${vacancy.id}"
                                       class="stretched-link text-decoration-none text-dark">
                                        ${vacancy.name}
                                    </a>
                                </h5>

                                <p class="mb-1">
                                    <@spring.message "vacancy.salary"/>:
                                    ${vacancy.salary!0}
                                </p>

                                <p class="text-muted mb-0">
                                    <@spring.message "vacancy.updated"/>:
                                    ${vacancyUpdateTimes[vacancy.id?string]!""}
                                </p>
                            </div>

                            <div class="d-flex gap-2 text-nowrap position-relative z-2">
                                <a href="/my-vacancies/${vacancy.id}/responses"
                                   class="btn btn-outline-dark btn-sm">
                                    <@spring.message "button.responses"/>
                                </a>

                                <a href="/my-vacancies/edit/${vacancy.id}"
                                   class="btn btn-outline-dark btn-sm">
                                    <@spring.message "button.edit"/>
                                </a>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="alert alert-light border">
                <@spring.message "profile.noVacancies"/>
            </div>
        </#if>
    </#if>
</@main.layout>