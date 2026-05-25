window.addEventListener('load', function () {
    const resumeForm = document.getElementById('resume-form');

    const contactList = document.getElementById('contact-list');
    const educationList = document.getElementById('education-list');
    const workList = document.getElementById('work-list');

    const addContactButton = document.getElementById('add-contact');
    const addEducationButton = document.getElementById('add-education');
    const addWorkButton = document.getElementById('add-work');

    let contactIndex = document.querySelectorAll('#contact-list .contact-block').length;
    let educationIndex = document.querySelectorAll('#education-list .education-block').length;
    let workIndex = document.querySelectorAll('#work-list .work-block').length;

    function bindRemoveButtons() {
        const removeButtons = document.querySelectorAll('.remove-block');

        removeButtons.forEach(function (button) {
            button.onclick = function () {
                button.closest('.border').remove();
            };
        });
    }

    addContactButton.addEventListener('click', function () {
        const block = document.createElement('div');
        block.className = 'contact-block border rounded p-3 mb-3';

        block.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="mb-0">${resumeForm.dataset.contactBlock}</h6>
                <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                    ${resumeForm.dataset.removeText}
                </button>
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.contactType}</label>
                <select name="contactInfos[${contactIndex}].typeId" class="form-select">
                    ${buildContactTypeOptions()}
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.contactValue}</label>
                <input type="text" name="contactInfos[${contactIndex}].contactValue" class="form-control">
            </div>
        `;

        contactList.appendChild(block);
        contactIndex++;
        bindRemoveButtons();
    });

    addEducationButton.addEventListener('click', function () {
        const block = document.createElement('div');
        block.className = 'education-block border rounded p-3 mb-3';

        block.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="mb-0">${resumeForm.dataset.educationBlock}</h6>
                <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                    ${resumeForm.dataset.removeText}
                </button>
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationInstitution}</label>
                <input type="text" name="educationInfos[${educationIndex}].institution" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationProgram}</label>
                <input type="text" name="educationInfos[${educationIndex}].program" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationStartDate}</label>
                <input type="date" name="educationInfos[${educationIndex}].startDate" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationEndDate}</label>
                <input type="date" name="educationInfos[${educationIndex}].endDate" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationDegree}</label>
                <input type="text" name="educationInfos[${educationIndex}].degree" class="form-control">
            </div>
        `;

        educationList.appendChild(block);
        educationIndex++;
        bindRemoveButtons();
    });

    addWorkButton.addEventListener('click', function () {
        const block = document.createElement('div');
        block.className = 'work-block border rounded p-3 mb-3';

        block.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="mb-0">${resumeForm.dataset.workBlock}</h6>
                <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                    ${resumeForm.dataset.removeText}
                </button>
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workYears}</label>
                <input type="number" name="workExperienceInfos[${workIndex}].years" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workCompany}</label>
                <input type="text" name="workExperienceInfos[${workIndex}].companyName" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workPosition}</label>
                <input type="text" name="workExperienceInfos[${workIndex}].position" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workResponsibilities}</label>
                <textarea name="workExperienceInfos[${workIndex}].responsibilities" class="form-control" rows="4"></textarea>
            </div>
        `;

        workList.appendChild(block);
        workIndex++;
        bindRemoveButtons();
    });

    function buildContactTypeOptions() {
        const firstSelect = document.querySelector('#contact-list select');

        if (!firstSelect) {
            return '';
        }

        return firstSelect.innerHTML;
    }

    resumeForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const formData = new FormData(resumeForm);
        const data = new URLSearchParams(formData);

        try {
            const response = await fetch(resumeForm.action, {
                method: 'POST',
                body: data
            });

            if (response.ok) {
                window.location.href = '/resumes';
            } else {
                alert(resumeForm.dataset.saveError);
            }
        } catch (error) {
            console.log(error);
            alert(resumeForm.dataset.requestError);
        }
    });

    bindRemoveButtons();
});