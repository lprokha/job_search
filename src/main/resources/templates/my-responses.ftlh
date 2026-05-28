<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">
            <@spring.message "responses.myTitle"/>
        </h2>

        <a href="/profile" class="btn btn-outline-dark">
            <@spring.message "button.backToProfile"/>
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
                                    ${response.vacancy.name!""}
                                </a>
                            </h5>

                            <p class="mb-1">
                                <span class="fw-semibold">
                                    <@spring.message "resume.name"/>:
                                </span>
                                ${response.resume.name!""}
                            </p>

                            <#if response.vacancy.author??>
                                <p class="mb-0 text-muted">
                                    <@spring.message "company.title"/>:
                                    ${response.vacancy.author.name!""} ${response.vacancy.author.surname!""}
                                </p>
                            </#if>
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
            <@spring.message "responses.empty"/>
        </div>
    </#if>
</@main.layout>