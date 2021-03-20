using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using Retrospector.Main.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Main
{
    public partial class MainWindow : IMainWindow
    {
        private readonly MainWindowViewModel _viewModel;

        public MainWindow(MainWindowViewModel viewModel)
        {
            InitializeComponent();
            _viewModel = viewModel;
            DataContext = _viewModel;
            Loaded += Load;
        }

        private void Load(object sender, RoutedEventArgs e)
        {
            foreach (var attribute in Enum.GetValues<RetrospectorAttribute>())
                searchResultTable.Columns.Add(new DataGridTextColumn
                {
                    Header = attribute.ToString(),
                    Binding = new Binding($"[{(int)attribute}]"),
                    IsReadOnly = true,
                    Width = DataGridLength.SizeToHeader
                });
        }
    }
}