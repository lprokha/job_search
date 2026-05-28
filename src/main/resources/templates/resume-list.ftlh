<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>
            <@spring.message "resumes.myTitle"/>
        </h2>

        <div class="d-flex gap-2">
            <a href="/responses/my" class="btn btn-outline-dark">
                <@spring.message "responses.myTitle"/>
            </a>

            <a href="/resumes/create" class="btn btn-dark">
                <@spring.message "button.addResume"/>
            </a>

            <a href="/profile" class="btn btn-outline-dark">
                <@spring.message "button.backToProfile"/>
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
                                <span class="fw-semibold">
                                    <@spring.message "vacancy.salary"/>:
                                </span>
                                ${resume.salary!0}
                            </p>

                            <p class="text-muted mb-0">
                                <@spring.message "vacancy.updated"/>:
                                ${resumeUpdateTimes[resume.id?string]!""}
                            </p>
                        </div>

                        <div class="text-nowrap d-flex gap-2">
                            <a href="/responses/my" class="btn btn-outline-dark btn-sm">
                                <@spring.message "button.responses"/>
                            </a>

                            <a href="/resumes/edit/${resume.id}" class="btn btn-outline-dark btn-sm">
                                <@spring.message "button.edit"/>
                            </a>
                        </div>
                    </div>
                </div>
            </#list>
        </div>

        <#if totalPages?? && (totalPages > 1)>
            <nav class="mt-4">
                <ul class="pagination">
                    <#list 0..(totalPages - 1) as pageIndex>
                        <li class="page-item <#if pageIndex == currentPage>active</#if>">
                            <a class="page-link" href="/resumes?page=${pageIndex}&size=5">
                                ${pageIndex + 1}
                            </a>
                        </li>
                    </#list>
                </ul>
            </nav>
        </#if>
    <#else>
        <div class="alert alert-light border">
            <@spring.message "resumes.notFound"/>
        </div>
    </#if>
</@main.layout>