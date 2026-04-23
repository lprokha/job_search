<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <h2>Компании</h2>

    <#list companies as company>
        <div class="card mb-2 p-3">
            <a href="/vacancies?authorId=${company.id}">
                ${company.name} ${company.surname}
            </a>
        </div>
    </#list>

</@main.layout>