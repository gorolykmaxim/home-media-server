import {Component, OnDestroy, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {DownloadsService} from './downloads.service';
import {ErrorService} from '../error/error.service';

@Component({
  selector: 'app-downloads',
  templateUrl: './downloads.component.html'
})
export class DownloadsComponent implements OnInit, OnDestroy {
  private downloadsObservable: Observable<any>;
  private intervalId: number;
  constructor(private downloadsService: DownloadsService, private errorService: ErrorService) {}
  async refresh() {
    try {
      await this.downloadsService.refresh();
    } catch (e) {
      this.errorService.notifyAbout(e, DownloadsComponent);
    }
  }
  ngOnInit(): void {
    this.errorService.registerFailingComponent(DownloadsComponent);
    this.downloadsObservable = this.downloadsService.getDownloadsObservable();
    this.refresh();
    this.intervalId = setInterval(this.refresh.bind(this), 5000);
  }
  ngOnDestroy(): void {
    this.errorService.unregisterFailingComponent(DownloadsComponent);
    clearInterval(this.intervalId);
  }
}
