import {EventEmitter, Injectable} from '@angular/core';
import {Restangular} from 'ngx-restangular';
import {ConnectivityService} from '../infrastructure/connectivity.service';
import {KeyObjectStorageService} from '../infrastructure/key-object-storage.service';

@Injectable({providedIn: 'root'})
export class DownloadsService {
  private downloadsObservable = new EventEmitter();
  private restangular: Restangular;
  private isOnline: boolean;
  private keyObjectStorage: KeyObjectStorageService
  constructor(restangular: Restangular, connectivityService: ConnectivityService, keyObjectStorage: KeyObjectStorageService) {
    this.restangular = restangular;
    this.isOnline = true;
    this.keyObjectStorage = keyObjectStorage;
    connectivityService.getIsOnlineObservable().subscribe(status => this.isOnline = status);
  }
  async refresh() {
    let downloads;
    if (this.isOnline) {
      downloads = await this.restangular.all('downloads').getList().toPromise();
      this.keyObjectStorage.save('downloads', downloads);
    } else {
      downloads = this.keyObjectStorage.get('downloads');
    }
    this.downloadsObservable.emit(downloads);
  }
  getDownloadsObservable() {
    return this.downloadsObservable;
  }
}
