// Person.js
// Person data model
export default class Person {
  constructor({ id, name, profile_path, popularity, known_for_department, known_for }) {
    this.id = id;
    this.name = name;
    this.profilePath = profile_path;
    this.popularity = popularity;
    this.department = known_for_department;
    this.knownFor = known_for;
  }
}
