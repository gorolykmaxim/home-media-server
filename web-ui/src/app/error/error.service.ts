import {EventEmitter, Injectable} from '@angular/core';
import {Router} from '@angular/router';

@Injectable({providedIn: 'root'})
export class ErrorService {
  private errorsObservables = {};
  private componentSubscriptions = {};
  private latestError: any = null;
  constructor(private router: Router) {}
  registerFailingComponent(component) {
    const observable = this.errorsObservables[component] = new EventEmitter();
    this.componentSubscriptions[component] = observable.subscribe(_ => this.router.navigateByUrl('/error'));
  }
  unregisterFailingComponent(component) {
    this.componentSubscriptions[component].unsubscribe();
    delete this.componentSubscriptions[component];
    delete this.errorsObservables[component];
  }
  notifyAbout(error, component) {
    this.latestError = error;
    const observable = this.errorsObservables[component];
    if (observable) {
      observable.emit(error);
    } else {
      console.error('User was not notified about:', error);
    }
  }
  getLatest() {
    return this.latestError;
  }
}
