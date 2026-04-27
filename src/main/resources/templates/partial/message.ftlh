<#import "../layouts/layout.ftlh" as main>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm border-0">
                <div class="card-body text-center">
                    <h2 class="mb-3">Смена пароля</h2>

                    <#if message??>
                        <div class="alert alert-success">${message}</div>
                    </#if>

                    <a href="/login" class="btn btn-dark">Перейти ко входу</a>
                </div>
            </div>
        </div>
    </div>
</@main.layout>