<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<#assign profileErrors = .data_model["org.springframework.validation.BindingResult.profileForm"]!>

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

    <div class="mb-4 d-flex flex-wrap gap-2">
        <button type="button"
                class="btn btn-dark btn-sm text-nowrap"
                data-bs-toggle="modal"
                data-bs-target="#editProfileModal">
            <@spring.message "profile.edit"/>
        </button>

        <#if user.accountType?string == "APPLICANT">
            <a href="/responses/my" class="btn btn-outline-dark btn-sm text-nowrap">
                <@spring.message "responses.myTitle"/>
            </a>
        </#if>
    </div>

    <#if user.accountType?string == "APPLICANT">

        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="mb-0">
                <@spring.message "resumes.myTitle"/>
            </h3>

            <div class="d-flex flex-wrap gap-2">
                <a href="/resumes/create" class="btn btn-dark btn-sm text-nowrap">
                    <@spring.message "button.add"/>
                </a>

                <a href="/resumes" class="btn btn-outline-dark btn-sm text-nowrap">
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

                                <p class="mb-1">
                                    <#if resume.isActive?? && resume.isActive>
                                        <span class="badge text-bg-success">
                                            <@spring.message "status.active"/>
                                        </span>
                                    <#else>
                                        <span class="badge text-bg-secondary">
                                            <@spring.message "status.inactive"/>
                                        </span>
                                    </#if>
                                </p>

                                <p class="text-muted mb-0">
                                    <@spring.message "vacancy.updated"/>:
                                    ${resumeUpdateTimes[resume.id?string]!""}
                                </p>
                            </div>

                            <div class="d-flex flex-wrap gap-2 text-nowrap">
                                <form action="/resumes/${resume.id}/refresh" method="post">
                                    <input type="hidden" name="redirectTo" value="/profile">

                                    <#if _csrf??>
                                        <input
                                                type="hidden"
                                                name="${(_csrf.parameterName)!'csrf'}"
                                                value="${(_csrf.token)!''}">
                                    </#if>

                                    <button type="submit"
                                            class="btn btn-outline-dark btn-sm text-nowrap">
                                        <@spring.message "button.refresh"/>
                                    </button>
                                </form>

                                <form action="/resumes/${resume.id}/toggle-active" method="post">
                                    <input type="hidden" name="redirectTo" value="/profile">

                                    <#if _csrf??>
                                        <input
                                                type="hidden"
                                                name="${(_csrf.parameterName)!'csrf'}"
                                                value="${(_csrf.token)!''}">
                                    </#if>

                                    <button type="submit"
                                            class="btn btn-outline-dark btn-sm text-nowrap">
                                        <#if resume.isActive?? && resume.isActive>
                                            <@spring.message "button.unpublish"/>
                                        <#else>
                                            <@spring.message "button.publish"/>
                                        </#if>
                                    </button>
                                </form>

                                <a href="/resumes/edit/${resume.id}"
                                   class="btn btn-outline-dark btn-sm text-nowrap">
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

            <div class="d-flex flex-wrap gap-2">
                <a href="/my-vacancies/create" class="btn btn-dark btn-sm text-nowrap">
                    <@spring.message "button.add"/>
                </a>

                <a href="/my-vacancies" class="btn btn-outline-dark btn-sm text-nowrap">
                    <@spring.message "button.viewAll"/>
                </a>
            </div>
        </div>

        <#if vacancies?? && (vacancies?size > 0)>
            <div class="list-group shadow-sm">
                <#list vacancies as vacancy>
                    <div class="list-group-item">
                        <div class="d-flex justify-content-between align-items-start gap-3">
                            <div>
                                <h5 class="mb-2">
                                    ${vacancy.name}
                                </h5>

                                <p class="mb-1">
                                    <@spring.message "vacancy.salary"/>:
                                    ${vacancy.salary!0}
                                </p>

                                <p class="mb-1">
                                    <#if vacancy.isActive?? && vacancy.isActive>
                                        <span class="badge text-bg-success">
                                            <@spring.message "status.active"/>
                                        </span>
                                    <#else>
                                        <span class="badge text-bg-secondary">
                                            <@spring.message "status.inactive"/>
                                        </span>
                                    </#if>
                                </p>

                                <p class="text-muted mb-0">
                                    <@spring.message "vacancy.updated"/>:
                                    ${vacancyUpdateTimes[vacancy.id?string]!""}
                                </p>
                            </div>

                            <div class="d-flex flex-wrap gap-2 text-nowrap">
                                <form action="/my-vacancies/${vacancy.id}/refresh" method="post">
                                    <input type="hidden" name="redirectTo" value="/profile">

                                    <#if _csrf??>
                                        <input
                                                type="hidden"
                                                name="${(_csrf.parameterName)!'csrf'}"
                                                value="${(_csrf.token)!''}">
                                    </#if>

                                    <button type="submit"
                                            class="btn btn-outline-dark btn-sm text-nowrap">
                                        <@spring.message "button.refresh"/>
                                    </button>
                                </form>

                                <form action="/my-vacancies/${vacancy.id}/toggle-active" method="post">
                                    <input type="hidden" name="redirectTo" value="/profile">

                                    <#if _csrf??>
                                        <input
                                                type="hidden"
                                                name="${(_csrf.parameterName)!'csrf'}"
                                                value="${(_csrf.token)!''}">
                                    </#if>

                                    <button type="submit"
                                            class="btn btn-outline-dark btn-sm text-nowrap">
                                        <#if vacancy.isActive?? && vacancy.isActive>
                                            <@spring.message "button.unpublish"/>
                                        <#else>
                                            <@spring.message "button.publish"/>
                                        </#if>
                                    </button>
                                </form>

                                <a href="/my-vacancies/${vacancy.id}/responses"
                                   class="btn btn-outline-dark btn-sm text-nowrap">
                                    <@spring.message "button.responses"/>
                                </a>

                                <a href="/my-vacancies/edit/${vacancy.id}"
                                   class="btn btn-outline-dark btn-sm text-nowrap">
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

    <div class="modal fade <#if showEditProfileModal?? && showEditProfileModal>show</#if>"
         id="editProfileModal"
         tabindex="-1"
         aria-hidden="<#if showEditProfileModal?? && showEditProfileModal>false<#else>true</#if>"
         <#if showEditProfileModal?? && showEditProfileModal>style="display: block;"</#if>>

        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form action="/profile/edit" method="post">
                    <#if _csrf??>
                        <input
                                type="hidden"
                                name="${(_csrf.parameterName)!'csrf'}"
                                value="${(_csrf.token)!''}">
                    </#if>

                    <div class="modal-header">
                        <h5 class="modal-title">
                            <@spring.message "profile.editTitle"/>
                        </h5>

                        <a href="/profile" class="btn-close"></a>
                    </div>

                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.name"/>
                                </label>

                                <input type="text"
                                       name="name"
                                       class="form-control"
                                       value="${profileForm.name!''}">

                                <#if profileErrors?has_content && profileErrors.hasFieldErrors("name")>
                                    <div class="text-danger small mt-1">
                                        ${profileErrors.getFieldError("name").defaultMessage}
                                    </div>
                                </#if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.surname"/>
                                </label>

                                <input type="text"
                                       name="surname"
                                       class="form-control"
                                       value="${profileForm.surname!''}">

                                <#if profileErrors?has_content && profileErrors.hasFieldErrors("surname")>
                                    <div class="text-danger small mt-1">
                                        ${profileErrors.getFieldError("surname").defaultMessage}
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.email"/>
                                </label>

                                <input type="email"
                                       name="email"
                                       class="form-control"
                                       value="${profileForm.email!''}">

                                <#if profileErrors?has_content && profileErrors.hasFieldErrors("email")>
                                    <div class="text-danger small mt-1">
                                        ${profileErrors.getFieldError("email").defaultMessage}
                                    </div>
                                </#if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.phoneNumber"/>
                                </label>

                                <input type="text"
                                       name="phoneNumber"
                                       class="form-control"
                                       value="${profileForm.phoneNumber!''}">

                                <#if profileErrors?has_content && profileErrors.hasFieldErrors("phoneNumber")>
                                    <div class="text-danger small mt-1">
                                        ${profileErrors.getFieldError("phoneNumber").defaultMessage}
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">
                                    <@spring.message "label.age"/>
                                </label>

                                <input type="number"
                                       name="age"
                                       class="form-control"
                                       value="${profileForm.age!''}">

                                <#if profileErrors?has_content && profileErrors.hasFieldErrors("age")>
                                    <div class="text-danger small mt-1">
                                        ${profileErrors.getFieldError("age").defaultMessage}
                                    </div>
                                </#if>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <a href="/profile" class="btn btn-outline-secondary">
                            <@spring.message "button.cancel"/>
                        </a>

                        <button type="submit" class="btn btn-dark">
                            <@spring.message "button.save"/>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <#if showEditProfileModal?? && showEditProfileModal>
        <div class="modal-backdrop fade show"></div>
    </#if>
</@main.layout>