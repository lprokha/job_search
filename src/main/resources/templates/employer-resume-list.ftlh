<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="d-flex flex-column flex-lg-row justify-content-between align-items-start align-items-lg-center gap-3 mb-4">

        <h2 class="mb-0">
            <@spring.message "resumes.all"/>
        </h2>

        <a href="/profile" class="btn btn-outline-dark btn-sm text-nowrap">
            <@spring.message "button.backToProfile"/>
        </a>
    </div>

    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body">
            <form method="get" action="/employer/resumes" class="row g-3 align-items-end">
                <input type="hidden" name="page" value="0">

                <div class="col-md-6">
                    <label for="categoryId" class="form-label">
                        <@spring.message "label.category"/>
                    </label>

                    <select name="categoryId" id="categoryId" class="form-select">
                        <option value="">
                            <@spring.message "resume.allCategories"/>
                        </option>

                        <#if categories??>
                            <#list categories as category>
                                <option value="${category.id}"
                                        <#if categoryId?? && categoryId == category.id>selected</#if>>
                                    ${category.name}
                                </option>
                            </#list>
                        </#if>
                    </select>
                </div>

                <div class="col-md-6 d-flex flex-wrap gap-2">
                    <button type="submit" class="btn btn-dark btn-sm text-nowrap">
                        <@spring.message "button.apply"/>
                    </button>

                    <a href="/employer/resumes" class="btn btn-outline-dark btn-sm text-nowrap">
                        <@spring.message "button.clear"/>
                    </a>
                </div>
            </form>
        </div>
    </div>

    <#if resumes?? && (resumes?size > 0)>
        <#list resumes as resume>
            <div class="card shadow-sm border-0 mb-3 position-relative">
                <div class="card-body">
                    <h4 class="mb-1">
                        <a href="/employer/resumes/${resume.id}"
                           class="stretched-link text-decoration-none text-dark">
                            ${resume.name}
                        </a>
                    </h4>

                    <p class="mb-1 fw-semibold">
                        <@spring.message "vacancy.salary"/>:
                        ${resume.salary!0}
                    </p>

                    <#if resume.applicant??>
                        <p class="mb-1">
                            <@spring.message "profile.applicant"/>:
                            ${resume.applicant.name!""} ${resume.applicant.surname!""}
                        </p>
                    </#if>

                    <p class="text-muted mb-2">
                        <@spring.message "vacancy.updated"/>:
                        ${resumeUpdateTimes[resume.id?string]!""}
                    </p>

                    <#if resume.category??>
                        <p class="mb-0">
                            <@spring.message "label.category"/>:
                            ${resume.category.name!""}
                        </p>
                    </#if>

                    <p class="small text-muted mt-2 mb-0">
                        <@spring.message "button.viewResumeDetails"/>
                    </p>
                </div>
            </div>
        </#list>

        <#if totalPages?? && (totalPages > 1)>
            <nav class="mt-4">
                <ul class="pagination">
                    <#list 0..(totalPages - 1) as pageIndex>
                        <li class="page-item <#if pageIndex == currentPage>active</#if>">
                            <a class="page-link"
                               href="/employer/resumes?page=${pageIndex}&size=5<#if categoryId??>&categoryId=${categoryId}</#if>">
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