import {EventEmitter, Injectable} from '@angular/core';
import {fromEvent} from 'rxjs';

@Injectable({providedIn: 'root'})
export class ConnectivityService {
  private readonly isOnlineObservable;
  constructor() {
    this.isOnlineObservable = new EventEmitter();
    fromEvent(window, 'online').subscribe(_ => this.isOnlineObservable.emit(true));
    fromEvent(window, 'offline').subscribe(_ => this.isOnlineObservable.emit(false));
  }
  getIsOnlineObservable() {
    return this.isOnlineObservable;
  }
}
