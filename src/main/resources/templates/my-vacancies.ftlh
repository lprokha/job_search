<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Мои вакансии</h2>

        <div class="d-flex gap-2">
            <form method="get" action="/my-vacancies" class="d-flex gap-2 align-items-center">
                <input type="hidden" name="page" value="0">

                <label for="sort" class="mb-0">Сортировка:</label>
                <select name="sort" id="sort" class="form-select" onchange="this.form.submit()">
                    <option value="dateDesc" <#if sort?? && sort == "dateDesc">selected</#if>>
                        По дате (сначала новые)
                    </option>
                    <option value="dateAsc" <#if sort?? && sort == "dateAsc">selected</#if>>
                        По дате (сначала старые)
                    </option>
                    <option value="responsesDesc" <#if sort?? && sort == "responsesDesc">selected</#if>>
                        По откликам (сначала больше)
                    </option>
                    <option value="responsesAsc" <#if sort?? && sort == "responsesAsc">selected</#if>>
                        По откликам (сначала меньше)
                    </option>
                </select>
            </form>

            <a href="/my-vacancies/create" class="btn btn-dark">Добавить вакансию</a>
            <a href="/profile" class="btn btn-outline-dark">Назад в личный кабинет</a>
        </div>
    </div>

    <#if vacancies?? && (vacancies?size > 0)>
        <div class="row">
            <#list vacancies as vacancy>
                <div class="col-md-6 col-lg-4 mb-3">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-body">
                            <h5>${vacancy.name}</h5>
                            <p class="mb-2">Зарплата: ${vacancy.salary!0}</p>
                            <p class="mb-2">Опыт: ${vacancy.expFrom!0} - ${vacancy.expTo!0} лет</p>
                            <p class="text-muted mb-2">Обновлено: ${vacancyUpdateTimes[vacancy.id?string]!""}</p>

                            <div class="d-flex gap-2">
                                <a href="/my-vacancies/edit/${vacancy.id}" class="btn btn-outline-dark btn-sm">
                                    Редактировать
                                </a>
                            </div>
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
                            <a class="page-link" href="/my-vacancies?page=${pageIndex}&size=5&sort=${sort!'dateDesc'}">
                                ${pageIndex + 1}
                            </a>
                        </li>
                    </#list>
                </ul>
            </nav>
        </#if>
    <#else>
        <div class="alert alert-light border">Вакансии не найдены.</div>
    </#if>
</@main.layout>