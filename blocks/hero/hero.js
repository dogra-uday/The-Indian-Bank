import { createOptimizedPicture } from '../../scripts/aem.js';

let pickerCount = 0;

function createFilePicker() {
  pickerCount += 1;
  const pickerId = `hero-image-picker-${pickerCount}`;

  const wrapper = document.createElement('div');
  wrapper.className = 'hero-filepicker';

  const label = document.createElement('label');
  label.className = 'hero-filepicker-label';
  label.setAttribute('for', pickerId);
  label.textContent = 'Choose a hero image from your filesystem';

  const input = document.createElement('input');
  input.className = 'hero-filepicker-input';
  input.id = pickerId;
  input.type = 'file';
  input.accept = 'image/*';

  const help = document.createElement('p');
  help.className = 'hero-filepicker-help';
  help.textContent = 'Pick a local image and it will preview here in this browser session.';

  const preview = document.createElement('div');
  preview.className = 'hero-filepicker-preview';
  preview.innerHTML = '<span>Hero image preview appears here</span>';

  input.addEventListener('change', () => {
    const [file] = input.files || [];
    if (!file) {
      preview.innerHTML = '<span>Hero image preview appears here</span>';
      return;
    }

    const objectUrl = URL.createObjectURL(file);
    preview.innerHTML = `<img src="${objectUrl}" alt="Selected hero image preview">`;
  });

  wrapper.append(label, input, help, preview);
  return wrapper;
}

export default function decorate(block) {
  const picture = block.querySelector('picture img');

  if (picture) {
    const optimizedPicture = createOptimizedPicture(picture.src, picture.alt, false, [
      { width: '2000' },
    ]);
    picture.closest('picture').replaceWith(optimizedPicture);
  }

  const enablePicker = block.classList.contains('filepicker') || !block.querySelector('picture');
  if (enablePicker) {
    block.classList.add('hero-with-filepicker');
    block.append(createFilePicker());
  }
}
