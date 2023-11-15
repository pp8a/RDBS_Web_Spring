// JavaScript для подсветки при наведении
        document.addEventListener('DOMContentLoaded', function () {
            const images = document.querySelectorAll('.features img');

            images.forEach(img => {
                img.addEventListener('mouseover', function () {
                    this.style.outline = '2px solid #000'; // Цвет подсветки (замените на ваш цвет)
                });

                img.addEventListener('mouseout', function () {
                    this.style.outline = 'none'; // Убираем подсветку при уходе курсора
                });
            });
        });