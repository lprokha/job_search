<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="text-center mb-4">Вход в аккаунт</h2>

                    <#if error??>
                        <div class="alert alert-danger">Неверный email или пароль</div>
                    </#if>

                    <form action="/login" method="post">
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="username" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Пароль</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>

                        <button type="submit" class="btn btn-dark w-100">Войти</button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="/register" class="text-decoration-none">Создать аккаунт</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@main.layout>