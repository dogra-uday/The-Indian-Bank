import { createOptimizedPicture } from '../../scripts/aem.js';

export default function decorate(block) {
  const picture = block.querySelector('picture img');

  if (picture) {
    const optimizedPicture = createOptimizedPicture(picture.src, picture.alt, false, [
      { width: '2000' },
    ]);
    picture.closest('picture').replaceWith(optimizedPicture);
  }
}
