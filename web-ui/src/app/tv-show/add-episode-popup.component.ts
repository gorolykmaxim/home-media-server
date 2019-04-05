import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from '@angular/material';
import {Restangular} from 'ngx-restangular';
import {TvShowService} from './tv-show.service';
import {ErrorService} from '../error/error.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-episode-popup',
  templateUrl: './add-episode-popup.component.html'
})
export class AddEpisodePopupComponent implements OnInit, OnDestroy {
  private episode: any = {};
  constructor(@Inject(MAT_BOTTOM_SHEET_DATA) private data: any, private bottomSheet: MatBottomSheetRef<AddEpisodePopupComponent>,
              private restangular: Restangular, private tvShowService: TvShowService, private errorService: ErrorService,
              private router: Router) {}
  async add() {
    try {
      await this.tvShowService.addEpisode(this.data.tvShow, this.episode);
      this.router.navigateByUrl('/downloads');
    } catch (e) {
      this.errorService.notifyAbout(e, AddEpisodePopupComponent);
    } finally {
      this.bottomSheet.dismiss();
    }
  }
  ngOnDestroy(): void {
    this.errorService.unregisterFailingComponent(AddEpisodePopupComponent);
  }
  ngOnInit(): void {
    this.errorService.registerFailingComponent(AddEpisodePopupComponent);
  }
}
