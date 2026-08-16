// PersonCard.js
// Renders a person card
export default class PersonCard {
  constructor(person, imageUrl) {
    this.person = person;
    this.imageUrl = imageUrl;
  }

  render() {
    const card = document.createElement('div');
    card.className = 'person-card';

    // Create image container
    const imageContainer = document.createElement('div');
    imageContainer.className = 'person-image-container';

    // Create image element
    const image = document.createElement('img');
    image.className = 'card-image';

    // Add loading attribute for better performance
    image.loading = 'lazy';

    // Set image source
    if (this.imageUrl && !this.imageUrl.includes('null')) {
      // Preload image to get dimensions
      const preloadImg = new Image();
      preloadImg.onload = () => {
        // If image is very wide or very tall, adjust container class
        const aspectRatio = preloadImg.width / preloadImg.height;
        if (aspectRatio > 1.2) {
          imageContainer.classList.add('wide-image');
        } else if (aspectRatio < 0.7) {
          imageContainer.classList.add('tall-image');
        }
        // Set the actual image source after preloading
        image.src = this.imageUrl;
      };
      preloadImg.onerror = () => {
        image.src = 'images/no-profile.png';
        imageContainer.classList.add('no-image');
      };
      preloadImg.src = this.imageUrl;
    } else {
      // Use a better avatar placeholder
      image.src = 'images/no-profile.png';
      imageContainer.classList.add('no-image');
    }

    image.alt = this.person.name || 'Unknown Person';

    // Handle image loading errors
    image.onerror = () => {
      image.src = 'images/no-profile.png';
      imageContainer.classList.add('no-image');
    };

    imageContainer.appendChild(image);
    card.appendChild(imageContainer);

    // Create person info container
    const personInfo = document.createElement('div');
    personInfo.className = 'person-info';

    // Add name
    const nameHeading = document.createElement('h3');
    nameHeading.textContent = this.person.name;
    personInfo.appendChild(nameHeading);

    // Add popularity score
    const popularityDiv = document.createElement('div');
    popularityDiv.className = 'person-popularity';
    popularityDiv.innerHTML = `<i class="fa-solid fa-fire"></i> ${this.person.popularity ? this.person.popularity.toFixed(1) : 'N/A'}`;
    personInfo.appendChild(popularityDiv);

    // Add department as a small badge
    const deptBadge = document.createElement('span');
    deptBadge.className = 'department-badge';
    deptBadge.textContent = this.person.department || 'Unknown';
    personInfo.appendChild(deptBadge);

    // Create empty person details div for spacing
    const detailsDiv = document.createElement('div');
    detailsDiv.className = 'person-details';
    personInfo.appendChild(detailsDiv);

    // Add known for section if available
    if (this.person.knownFor && this.person.knownFor.length > 0) {
      const knownForDiv = document.createElement('div');
      knownForDiv.className = 'known-for';

      const knownForTitle = document.createElement('h4');
      knownForTitle.className = 'known-for-title';
      knownForTitle.textContent = 'Known For';
      knownForDiv.appendChild(knownForTitle);

      const knownForList = document.createElement('ul');
      knownForList.className = 'known-for-list';

      // Limit to max 5 items to keep card size consistent
      const limitedKnownFor = this.person.knownFor.slice(0, 5);

      limitedKnownFor.forEach(item => {
        const type = item.media_type === 'movie' ? 'Movie' : 'TV';
        const title = item.title || item.name || 'Unknown';

        const listItem = document.createElement('li');
        const typeSpan = document.createElement('span');
        typeSpan.className = `media-type ${item.media_type}`;
        typeSpan.textContent = type;

        const titleSpan = document.createElement('span');
        titleSpan.className = 'media-title';
        titleSpan.textContent = title;

        listItem.appendChild(typeSpan);
        listItem.appendChild(document.createTextNode(': '));
        listItem.appendChild(titleSpan);

        // Add tooltip for full title
        listItem.title = `${type}: ${title}`;
        knownForList.appendChild(listItem);
      });

      knownForDiv.appendChild(knownForList);
      personInfo.appendChild(knownForDiv);
    }
    card.appendChild(personInfo);
    return card;
  }
}
