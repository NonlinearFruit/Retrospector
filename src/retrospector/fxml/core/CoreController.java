
            UndecoratorScene.setClassicDecoration();
            UndecoratorScene undecoratorScene = new UndecoratorScene(stage, (Region) root);
            stage.setScene(undecoratorScene);
            stage.setScene(undecoratorScene);
            stage.setTitle("About");
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    @FXML
    public void scrape(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/scraper/Scraper.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            ScraperController scrc = fxmlLoader.getController();
            scrc.setup(media -> { 
                int id = DataManager.createDB(media); 
                media.setId(id);
                setMedia( media ); 
                setTab( TAB.MEDIA ); 
            });
            Stage stage = new Stage();
            stage.setTitle("Scraper");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception ex) {
            System.out.println("Scraper Failed: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
   
    @FXML 
    public void backup(ActionEvent e) { 
        DataManager.makeBackup(); 
        Toast.makeText((Stage) menuBar.getScene().getWindow(), "Backup Complete!", 3000, 500, 500);
    }
    
    @FXML
    public void cheatsheet(ActionEvent e) { new Cheatsheet().start(new Stage()); }
    
    @FXML
    public void exit() { exit(null); }
    
    public void exit(ActionEvent e) {
        Platform.exit();
    }
    
    public void setTab(TAB aTab){
        currentTab.set(aTab);
    }
    
    public void setMedia(Media m){
        currentMedia.set(m);
    }
    
    protected void nextPreviousMedia(Integer i){
        if(i>0)
            searchController.next();
        else
            searchController.previous();
    }
}
