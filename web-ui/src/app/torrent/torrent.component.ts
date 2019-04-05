import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatBottomSheet, MatDialog} from '@angular/material';
import {ConfirmationDialogComponent} from '../common/confirmation-dialog.component';
import {DownloadTorrentPopupComponent} from './download-torrent-popup.component';
import {Observable} from 'rxjs';
import {TorrentService} from './torrent.service';
import {ErrorService} from '../error/error.service';

@Component({
  selector: 'app-torrent',
  templateUrl: './torrent.component.html'
})
export class TorrentComponent implements OnInit, OnDestroy {
  private torrentsObservable: Observable<any>;
  private intervalId: number;
  constructor(private bottomSheet: MatBottomSheet, private dialog: MatDialog, private torrentService: TorrentService,
              private errorService: ErrorService) {}
  addTorrent() {
    this.bottomSheet.open(DownloadTorrentPopupComponent);
  }
  removeTorrent(torrent) {
    const dialog = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: `Delete ${torrent.name}`,
        message: 'Are you sure you want to delete this torrent?'
      }
    });
    dialog.afterClosed().subscribe(async result => {
      if (result) {
        try {
          await this.torrentService.removeTorrent(torrent);
        } catch (e) {
          this.errorService.notifyAbout(e, TorrentComponent);
        }
      }
    });
  }
  async refresh() {
    try {
      await this.torrentService.refresh();
    } catch (e) {
      this.bottomSheet.dismiss();
      this.dialog.closeAll();
      this.errorService.notifyAbout(e, TorrentComponent);
    }
  }
  ngOnInit(): void {
    this.errorService.registerFailingComponent(TorrentComponent);
    this.torrentsObservable = this.torrentService.getTorrentsObservable();
    this.refresh();
    this.intervalId = setInterval(this.refresh.bind(this), 5000);
  }
  ngOnDestroy(): void {
    this.errorService.unregisterFailingComponent(TorrentComponent);
    clearInterval(this.intervalId);
  }
}
