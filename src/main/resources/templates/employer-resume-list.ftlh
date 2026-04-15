<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Все резюме</h2>
        <a href="/profile" class="btn btn-outline-dark">Назад в личный кабинет</a>
    </div>

    <#if resumes?? && (resumes?size > 0)>
        <div class="row">
            <#list resumes as resume>
                <div class="col-md-6 col-lg-4 mb-3">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-body">
                            <h5>${resume.name}</h5>
                            <p class="mb-2">Зарплата: ${resume.salary!0}</p>
                            <p class="text-muted mb-2">Обновлено: ${resumeUpdateTimes[resume.id?string]!""}</p>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    <#else>
        <div class="alert alert-light border">Резюме не найдены.</div>
    </#if>
</@main.layout>