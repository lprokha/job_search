<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Все резюме</h2>
        <a href="/profile" class="btn btn-outline-dark">Назад в личный кабинет</a>
    </div>

    <#if resumes?? && (resumes?size > 0)>
        <div class="row">
            <#list resumes as resume>
                <div class="col-md-6 col-lg-4 mb-3">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-body d-flex flex-column">
                            <h5 class="mb-2">${resume.name}</h5>
                            <p class="mb-2 fw-semibold">Зарплата: ${resume.salary!0}</p>
                            <p class="text-muted mb-3">Обновлено: ${resumeUpdateTimes[resume.id?string]!""}</p>
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
                            <a class="page-link" href="/employer/resumes?page=${pageIndex}&size=5">
                                ${pageIndex + 1}
                            </a>
                        </li>
                    </#list>
                </ul>
            </nav>
        </#if>
    <#else>
        <div class="alert alert-light border">Резюме не найдены.</div>
    </#if>
</@main.layout>