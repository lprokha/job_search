<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="mb-4">Редактирование профиля</h2>

                    <form>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Имя</label>
                                <input type="text" class="form-control" value="${user.name!''}">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Фамилия</label>
                                <input type="text" class="form-control" value="${user.surname!''}">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" value="${user.email!''}">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Номер телефона</label>
                                <input type="text" class="form-control" value="${user.phoneNumber!''}">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Возраст</label>
                                <input type="number" class="form-control" value="${user.age!''}">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Новый пароль</label>
                                <input type="password" class="form-control" placeholder="Введите новый пароль">
                            </div>
                        </div>

                        <button type="submit" class="btn btn-dark">Сохранить</button>
                        <a href="/profile" class="btn btn-outline-secondary ms-2">Назад</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>