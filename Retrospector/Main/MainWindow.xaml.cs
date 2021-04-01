using Retrospector.Main.Interfaces;

namespace Retrospector.Main
{
    public partial class MainWindow : IMainWindow
    {
        public MainWindow(MainWindowViewModel viewModel)
        {
            InitializeComponent();
            DataContext = viewModel;
        }
    }
}