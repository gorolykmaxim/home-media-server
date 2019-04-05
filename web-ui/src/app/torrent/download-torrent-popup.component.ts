import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatBottomSheetRef} from '@angular/material';
import {TorrentService} from './torrent.service';
import {ErrorService} from '../error/error.service';

@Component({
  selector: 'app-download-torrent',
  templateUrl: './download-torrent-popup.component.html'
})
export class DownloadTorrentPopupComponent implements OnInit, OnDestroy {
  private torrent = {
    magnetLink: '',
    downloadFolder: '/downloads/'
  };
  constructor(private bottomSheet: MatBottomSheetRef<DownloadTorrentPopupComponent>,
              private torrentService: TorrentService, private errorService: ErrorService) {}
  async download() {
    try {
      await this.torrentService.addTorrent(this.torrent);
    } catch (e) {
      this.errorService.notifyAbout(e, DownloadTorrentPopupComponent);
    } finally {
      this.bottomSheet.dismiss();
    }
  }
  ngOnDestroy(): void {
    this.errorService.unregisterFailingComponent(DownloadTorrentPopupComponent);
  }
  ngOnInit(): void {
    this.errorService.registerFailingComponent(DownloadTorrentPopupComponent);
  }
}
