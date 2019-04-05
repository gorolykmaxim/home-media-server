import {Injectable} from '@angular/core';

@Injectable({providedIn: 'root'})
export class KeyObjectStorageService {
  save(key, object) {
    localStorage.setItem(key, JSON.stringify(object));
  }
  get(key) {
    return JSON.parse(localStorage.getItem(key));
  }
}
