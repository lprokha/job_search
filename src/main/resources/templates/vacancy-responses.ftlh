<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="mb-1">
                <@spring.message "responses.vacancyTitle"/>
            </h2>

            <p class="text-muted mb-0">
                ${vacancy.name!""}
            </p>
        </div>

        <a href="/my-vacancies" class="btn btn-outline-dark">
            <@spring.message "button.back"/>
        </a>
    </div>

    <#if responses?? && (responses?size > 0)>
        <div class="list-group shadow-sm">
            <#list responses as response>
                <div class="list-group-item list-group-item-action position-relative">
                    <div class="d-flex justify-content-between align-items-start gap-3">
                        <div>
                            <h5 class="mb-2">
                                <a href="/chat/${response.id}"
                                   class="stretched-link text-decoration-none text-dark">
                                    ${response.resume.name!""}
                                </a>
                            </h5>

                            <#if response.resume.applicant??>
                                <p class="mb-1">
                                    <span class="fw-semibold">
                                        <@spring.message "profile.applicant"/>:
                                    </span>
                                    ${response.resume.applicant.name!""} ${response.resume.applicant.surname!""}
                                </p>
                            </#if>

                            <p class="mb-0 text-muted">
                                <@spring.message "vacancy.salary"/>:
                                ${response.resume.salary!0}
                            </p>
                        </div>

                        <div class="text-nowrap position-relative z-2">
                            <a href="/chat/${response.id}" class="btn btn-outline-dark btn-sm">
                                <@spring.message "button.openChat"/>
                            </a>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    <#else>
        <div class="alert alert-light border">
            <@spring.message "responses.emptyForVacancy"/>
        </div>
    </#if>
</@main.layout>