<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-9">

            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body p-4">
                    <div class="d-flex justify-content-between align-items-start gap-3">
                        <div>
                            <h2 class="mb-2">${resume.name!""}</h2>

                            <p class="mb-1">
                                <span class="fw-semibold">
                                    <@spring.message "label.category"/>:
                                </span>
                                ${(resume.category.name)!""}
                            </p>

                            <p class="mb-1">
                                <span class="fw-semibold">
                                    <@spring.message "vacancy.salary"/>:
                                </span>
                                ${resume.salary!0}
                            </p>

                            <p class="text-muted mb-0">
                                <@spring.message "vacancy.updated"/>:
                                ${updatedAt!""}
                            </p>
                        </div>

                        <#if currentUser?? && currentUser.accountType?string == "APPLICANT" && resume.applicantId == currentUser.id>
                            <a href="/resumes/edit/${resume.id}" class="btn btn-outline-dark btn-sm">
                                <@spring.message "button.edit"/>
                            </a>
                        </#if>
                    </div>
                </div>
            </div>

            <#if resume.applicant??>
                <div class="card shadow-sm border-0 mb-4">
                    <div class="card-body p-4">
                        <h4 class="mb-3">
                            <@spring.message "profile.applicant"/>
                        </h4>

                        <p class="mb-1">
                            <span class="fw-semibold">
                                <@spring.message "label.name"/>:
                            </span>
                            ${resume.applicant.name!""} ${resume.applicant.surname!""}
                        </p>

                        <p class="mb-1">
                            <span class="fw-semibold">
                                <@spring.message "label.age"/>:
                            </span>
                            ${resume.applicant.age!""}
                        </p>

                        <p class="text-muted mb-0">
                            ${resume.applicant.email!""}
                        </p>
                    </div>
                </div>
            </#if>

            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body p-4">
                    <h4 class="mb-3">
                        <@spring.message "resume.contacts"/>
                    </h4>

                    <#if contactInfos?? && (contactInfos?size > 0)>
                        <div class="list-group">
                            <#list contactInfos as contact>
                                <div class="list-group-item">
                                    <span class="fw-semibold">
                                        ${(contact.type.type)!""}:
                                    </span>
                                    ${contact.contactValue!""}
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <p class="text-muted mb-0">
                            <@spring.message "resume.noContacts"/>
                        </p>
                    </#if>
                </div>
            </div>

            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body p-4">
                    <h4 class="mb-3">
                        <@spring.message "resume.education"/>
                    </h4>

                    <#if educationInfos?? && (educationInfos?size > 0)>
                        <div class="list-group">
                            <#list educationInfos as education>
                                <div class="list-group-item">
                                    <h6 class="mb-1">${education.institution!""}</h6>

                                    <p class="mb-1">
                                        ${education.program!""}
                                    </p>

                                    <p class="mb-1">
                                        ${education.degree!""}
                                    </p>

                                    <p class="text-muted mb-0">
                                        <#if education.startDate??>${education.startDate}</#if>
                                        <#if education.endDate??> - ${education.endDate}</#if>
                                    </p>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <p class="text-muted mb-0">
                            <@spring.message "resume.noEducation"/>
                        </p>
                    </#if>
                </div>
            </div>

            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body p-4">
                    <h4 class="mb-3">
                        <@spring.message "resume.workExperience"/>
                    </h4>

                    <#if workExperienceInfos?? && (workExperienceInfos?size > 0)>
                        <div class="list-group">
                            <#list workExperienceInfos as work>
                                <div class="list-group-item">
                                    <h6 class="mb-1">
                                        ${work.position!""}
                                    </h6>

                                    <p class="mb-1">
                                        <span class="fw-semibold">
                                            ${work.companyName!""}
                                        </span>
                                    </p>

                                    <p class="mb-1">
                                        <@spring.message "resume.years"/>:
                                        ${work.years!0}
                                    </p>

                                    <p class="mb-0">
                                        ${work.responsibilities!""}
                                    </p>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <p class="text-muted mb-0">
                            <@spring.message "resume.noWorkExperience"/>
                        </p>
                    </#if>
                </div>
            </div>

            <div class="d-flex gap-2">
                <#if currentUser?? && currentUser.accountType?string == "EMPLOYER">
                    <a href="/employer/resumes" class="btn btn-outline-dark">
                        <@spring.message "button.back"/>
                    </a>
                <#else>
                    <a href="/resumes" class="btn btn-outline-dark">
                        <@spring.message "button.back"/>
                    </a>
                </#if>
            </div>

        </div>
    </div>
</@main.layout>