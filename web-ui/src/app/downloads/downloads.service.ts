import {EventEmitter, Injectable} from '@angular/core';
import {Restangular} from 'ngx-restangular';

@Injectable({providedIn: 'root'})
export class DownloadsService {
  private downloadsObservable = new EventEmitter();
  constructor(private restangular: Restangular) {}
  async refresh() {
    const downloads = await this.restangular.all('downloads').getList().toPromise();
    this.downloadsObservable.emit(downloads);
  }
  getDownloadsObservable() {
    return this.downloadsObservable;
  }
}
